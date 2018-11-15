/**
  * Created by oguz on 31.10.2018.
  */
import java.awt.{Color, Dimension}

import scala.swing.event.{Key, KeyPressed, KeyReleased, KeyTyped}
import scala.swing.{BoxPanel, Graphics2D, MainFrame, Orientation, Panel, SimpleSwingApplication};

class GameCanvas(val height:Int, val width:Int, val cubeSize:Int) extends Panel{
  var board:GameArea = null
  preferredSize=new Dimension(width*cubeSize,height*cubeSize)

  /**
    * Refresh the board UI
    * @param g
    */
  override def paint(g: Graphics2D): Unit = {
    if (board == null) return

    for(y <- 0 until height; x <- 0 until width){
      var color = Color.BLACK
      board.get(y, x) match {
        case 0 => color = if (y < 4) Color.ORANGE else Color.GRAY
        case 1 => color = Color.BLUE
        case 2 => color = Color.RED
      }
      g.setColor(color)
      g.fillRect(x*cubeSize, y*cubeSize, cubeSize-1,cubeSize-1)
    }
  }
}

class GameUI(val height:Int, val width:Int) extends SimpleSwingApplication{

  var gameLogic:GameLogic=null

  val cubeSize= 25
  val canvas = new GameCanvas(height, width, cubeSize)

  def setGameLogic(gameLogic: GameLogic){this.gameLogic=gameLogic}

  /**
    * Update canvas and UI
    * @param board
    */
  def updateCanvas(board: GameArea): Unit = {canvas.board=board; canvas.repaint}

  def top = new MainFrame {
    contents = new BoxPanel(Orientation.Vertical) {
      listenTo(keys)
      reactions += {
        case KeyTyped(_, 'd', _, _) => gameLogic.right
        case KeyTyped(_, 's', _, _) => gameLogic.down
        case KeyTyped(_, 'a', _, _) => gameLogic.left
        case KeyTyped(_, 'e', _, _) => gameLogic.rotate
        case KeyTyped(_, ' ', _, _) => gameLogic.drop
      }
      contents += canvas
      focusable = true
      requestFocus
      preferredSize = new Dimension(cubeSize*width, cubeSize*height)
      resizable=false
    }
  }



}