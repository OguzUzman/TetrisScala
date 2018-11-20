import java.awt.{Color, Dimension}

import scala.swing.{Graphics2D, Panel}

/**
  * Created by oguz on 16.11.2018.
  */
class GameCanvas(val height:Int, val width:Int, val cubeSize:Int) extends Panel with GameBoardObserver{
  preferredSize=new Dimension(width*cubeSize,height*cubeSize)
  GlobalReactors.addGameBoardListener(this)
  /**
    * Refresh the board UI
    * @param g
    */
  override def paint(g: Graphics2D): Unit = {
    val gameBoardProvider:GameBoardProvider = GlobalReactors.gameBoardProvider
    if (gameBoardProvider == null) return
    for(y <- 0 until height; x <- 0 until width){
      var color = Color.BLACK
      gameBoardProvider.getCell(y, x) match {
        case 0 => color = if (y < 4) Color.ORANGE else Color.GRAY
        case 1 => color = Color.BLUE
        case 2 => color = Color.RED
      }
      g.setColor(color)
      g.fillRect(x*cubeSize, y*cubeSize, cubeSize-1,cubeSize-1)
    }
  }

  override def onGameBoardUpdate(gameBoard: GameBoard): Unit = repaint
}