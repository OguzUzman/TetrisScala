/**
  * Created by oguz on 31.10.2018.
  */
import java.awt.{Dimension}

import scala.swing.event.KeyTyped
import scala.swing.{BoxPanel, MainFrame, Orientation, SimpleSwingApplication};

class GameUI(val height:Int, val width:Int) extends SimpleSwingApplication{


  val cubeSize= 25
  val canvas = new GameCanvas(height, width, cubeSize)


  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      listenTo(keys)
      reactions += {
        case KeyTyped(_, 'd', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.right)
        case KeyTyped(_, 's', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.down)
        case KeyTyped(_, 'a', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.left)
        case KeyTyped(_, 'e', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.rotate)
        case KeyTyped(_, 'q', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.undo)
        case KeyTyped(_, 'r', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.newGame)
        case KeyTyped(_, 'x', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.exit)
        case KeyTyped(_, ' ', _, _) => GlobalReactors.gameControlObservers.foreach((listener: GameControlListener) => listener.drop)
      }
      contents += canvas
      focusable = true
      requestFocus
      preferredSize = new Dimension(cubeSize*width, cubeSize*height)
      resizable=false
    }
  }
}