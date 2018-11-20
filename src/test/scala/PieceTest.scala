import java.util.{Timer, TimerTask}

import org.scalatest.FlatSpec

/**
  * Created by oguz on 16.11.2018.
  */
class PieceTest extends FlatSpec{

  // We can reuse the same GameBoard class
  val height:Int = 20
  val width:Int = 40
  val gameUI:GameUI = new GameUI(height, width)
  val board:GameBoard = new GameBoard(height, width)
  val pieces = List(new DogLeft, new DogRight, new LeftL, new RightL, new Square, new Stick, new T)
  for (pieceIndex:Int <- 0 until 7){
    var piece:Piece = pieces(pieceIndex)
    for (rotation:Int <- 0 until 4) {
      println(piece)
      board.piece = piece
      piece.yx_(rotation*5, pieceIndex*5)
      board.fix
      piece = piece.rotate
    }
    println("")
  }
  gameUI.main(Array())
  val gameControlListener = new GameControlListener {override def rotate = ???

    override def down = ???

    override def drop = ???

    override def undo = ???

    override def newGame = ???

    override def left = ???

    override def right = ???
  }
  GlobalReactors.addGameControlListener(gameControlListener)
  while(true) Thread.sleep(1000000)

}

