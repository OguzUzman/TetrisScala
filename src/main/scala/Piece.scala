import scala.util.Random

/**
  * Created by oguz on 31.10.2018.
  */
class Piece {

  def size():Int = this.state.length

  private var _x=0
  private var _y=0
  var state: Array[Array[Int]] = _

  def x:Int = _x
  def y:Int = _y

  def x_(newX:Int):Piece = {_x = newX;this}
  def y_(newY:Int):Unit ={_y = newY;this}


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

  override def equals(other: Any): Boolean =
    other.asInstanceOf[Piece].state.
      zip(this.state).
      forall((tuple: (Array[Int], Array[Int])) => tuple._1.sameElements(tuple._2))


  override def toString =
    "_"*(state.length+2) +"\n" +
      state.map((row: Array[Int]) => "|"+row.map((i:Int)=>if (i == 1) '\u25A0' else " ").mkString("")+"|").mkString("\n")+
      "\n"+ "‾"*(state.length+2)

  override def clone(): Piece = {
    val copyElement = new Piece
    copyElement.state = state.map(_.clone)
    copyElement._x = x
    copyElement._y = y
    copyElement
  }
}


object PieceGenerator{

  val random =new Random()

  /**
    * Returns a random piece; selection is uniform
    * @return
    */
  def getRandom(): Piece =
      List(new Stick(), new Square(), new T(), new LeftL(), new RightL(), new DogLeft(), new DogRight())(random.nextInt(7))

}

class Stick extends Piece{

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

class T extends Piece{
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
    Array(1, 1, 0),
    Array(0, 1, 1),
    Array(0, 0, 0)
  )
}

class DogRight extends Piece{

  state = Array(
    Array(0, 1, 1),
    Array(1, 1, 0),
    Array(0, 0, 0)
  )
}