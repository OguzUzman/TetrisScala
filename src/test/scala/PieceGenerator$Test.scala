import org.scalatest.{FlatSpec, FunSuite}

/**
  * Created by oguz on 16.11.2018.
  */
class PieceGenerator$Test extends FlatSpec {

  var gameBoardProvider:GameBoardProvider=_
  gameBoardProvider.getCell(0,1)

  def testRotations(testPiece: Piece, expectedSize:Int): Unit={
    var piece = testPiece
    println(piece.getClass.getName)
    for (j <- 0 to 4){
      assert(piece.size() == expectedSize)
      println(piece)
      println()
      piece = piece.rotate
    }

    assert( piece.equals(piece.rotate.rotate.rotate.rotate))

  }

  List(
    (new Stick(), 4),
    (new Square(),2),
    (new T(), 3),
    (new LeftL(), 3),
    (new RightL(), 3),
    (new DogLeft(), 3),
    (new DogRight(),3)).foreach((tuple: (Piece, Int)) =>testRotations(tuple._1, tuple._2))

}
