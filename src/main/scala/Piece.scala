import scala.util.Random

/**
  * Created by oguz on 31.10.2018.
  */
class Piece {

  def size():Int = this.state.length

  private var _x=0
  private var _y=0

  def x:Int = _x
  def y:Int = _y

  def x_(newX:Int):Unit = _x = newX
  def y_(newY:Int):Unit = _y = newY

  def yx_ (y:Int, x:Int) :Unit = {y_(y); x_(x)}


  /**
    * Rotates the piece if allowed
    * @return
    */
  def rotate:Piece={
    new Piece().setState(rotate90(state))
  }

  // transpose and rotate90 are taken from https://gist.github.com/polyglotpiglet/6d10d7b69ecdbe5da5e4
  /**
    * Simple matrix transpose
    * @param matrix
    * @return
    */
  private def transpose(matrix: Array[Array[Int]]): Array[Array[Int]] = {
    matrix.head.indices.map(i => matrix.map(_(i))).toArray
  }

  private def rotate90(matrix: Array[Array[Int]]): Array[Array[Int]] = {
    transpose(matrix).map(_.reverse)
  }

  var state: Array[Array[Int]] = _

  /**
    * Sets the new configuration of the piece; called after rotation.
    * @param newState
    * @return
    */
  def setState(newState: Array[Array[Int]]):Piece={
    this.state=newState
    this
  }



  /**
    * Checks if the yth vertically, xth horizontally is occupied
    * @param y
    * @param x
    * @return
    */
  def get(y:Int,x:Int)={
    try{
      state(y)(x)
    }
    catch {
      case e:Exception=>e.printStackTrace()
    }
  }

}

object RandomPieceGenerator{
  val random =new Random()

  /**
    * Returns a random piece; selection is uniform
    * @return
    */
  def getNew(): Piece ={
      List(new Line(), new Square(), new TheT(), new LeftL(), new RightL(), new DogLeft(), new DogRight())(random.nextInt(7))

  }
}

class Line extends Piece{

  state = Array(
    Array(0, 1, 0, 0),
    Array(0, 1, 0, 0),
    Array(0, 1, 0, 0),
    Array(0, 1, 0, 0)
  )
}

class Square extends Piece{
  state = Array(
    Array(1, 1),
    Array(1, 1)
  )
}

class TheT extends Piece{
  state = Array(
    Array(0, 0, 0),
    Array(0, 1, 0),
    Array(1, 1, 1)
  )
}

class LeftL extends Piece{
  state = Array(
    Array(0, 1, 1),
    Array(0, 1, 0),
    Array(0, 1, 0)
  )
}

class RightL extends Piece{
  state = Array(
    Array(0, 1, 0),
    Array(0, 1, 0),
    Array(0, 1, 1)
  )
}

class DogLeft extends Piece{
  state = Array(
    Array(0, 1, 1),
    Array(1, 1, 0),
    Array(0, 0, 0)
  )
}

class DogRight extends Piece{

  state = Array(
    Array(1, 1, 0),
    Array(0, 1, 1),
    Array(0, 0, 0)
  )
}