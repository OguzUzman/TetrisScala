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

  def yx_ (y:Int, x:Int) :Piece = {y_(y); x_(x); this}


  /**
    * Rotates the piece if allowed
    * @return
    */
  def rotate:Piece={
    new Piece().setState(rotateCounter90(state)).yx_(y, x)
  }

  //
  /**
    * Matrix counter clockwise rotation
    * @param matrix
    * @return
    */
  private def rotateCounter90(matrix: Array[Array[Int]]): Array[Array[Int]] = {
    matrix.map(_.reverse).transpose
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
    *
    * @return the last horizontal which there is a cube
    */
  def maxHorizontal: Int = {
    0
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

  override def equals(other: Any): Boolean =
    other.asInstanceOf[Piece].state.
      zip(this.state).
      forall((tuple: (Array[Int], Array[Int])) => tuple._1.sameElements(tuple._2))
}


object PieceGenerator{

  val random =new Random()

  /**
    * Returns a random piece; selection is uniform
    * @return
    */
  def getRandom(): Piece =
      List(new Line(), new Square(), new TheT(), new LeftL(), new RightL(), new DogLeft(), new DogRight())(random.nextInt(7))

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