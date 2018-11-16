import org.scalatest.{FlatSpec, FunSuite}

/**
  * Created by oguz on 16.11.2018.
  */
class PieceGenerator$Test extends FlatSpec {


  def testRotations(testPiece: Piece): Unit={
    var piece = testPiece
    println(piece.getClass.getName)
    for (j <- 0 to 4){
      println(piece)
      println()
      piece = piece.rotate
    }

    assert( piece.equals(piece.rotate.rotate.rotate.rotate))

  }


  List(new Stick(), new Square(), new T(), new LeftL(), new RightL(), new DogLeft(), new DogRight()).
    foreach((piece: Piece) => testRotations(piece))
  val square = new Square
  val t = new T

}
