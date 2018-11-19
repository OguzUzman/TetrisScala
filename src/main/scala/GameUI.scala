/**
  * Created by oguz on 31.10.2018.
  */
import java.awt.{Color, Dimension}

import scala.swing.event.{Key, KeyPressed, KeyReleased, KeyTyped}
import scala.swing.{BoxPanel, Graphics2D, MainFrame, Orientation, Panel, SimpleSwingApplication};

class GameUI(val height:Int, val width:Int) extends SimpleSwingApplication{


  val cubeSize= 25
  val canvas = new GameCanvas(height, width, cubeSize)
  var gameControlListeners = List[GameControlListener]()


  /**
    * Update canvas and UI
    */
  def updateCanvas: Unit = {canvas.repaint}

  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      listenTo(keys)
      reactions += {
        case KeyTyped(_, 'd', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.right)
        case KeyTyped(_, 's', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.down)
        case KeyTyped(_, 'a', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.left)
        case KeyTyped(_, 'e', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.rotate)
        case KeyTyped(_, 'q', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.undo)
        case KeyTyped(_, 'r', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.newGame)
        case KeyTyped(_, ' ', _, _) => GlobalReactors.gameControlListeners.foreach((listener: GameControlListener) => listener.drop)
      }
      contents += canvas
      focusable = true
      requestFocus
      preferredSize = new Dimension(cubeSize*width, cubeSize*height)
      resizable=false
    }
  }

  def addGameControlListener(gameControlListener: GameControlListener):Unit=
    gameControlListeners = gameControlListeners :+ gameControlListener



}