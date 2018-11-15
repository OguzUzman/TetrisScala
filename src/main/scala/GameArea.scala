/**
  * Created by oguz on 31.10.2018.
  */
class GameArea(val height:Int, val width:Int) {

  var board = Array.ofDim[Int](height, width)
  var piece:Piece=_
  // var pieceLocation=(0, width/2):(Int, Int)

  /**
    * Random piece at the top of the board horizontally center
    */
  def newPiece={
    piece = PieceGenerator.getRandom().yx_(0, (width-2)/2)
  }
  /**
    * Fixes the piece onto the board.
    */
  def fix={
    var copyMap = board.clone()
    for (j <- 0 until piece.size; i <- 0 until piece.size){
      if (piece.y+j<height && piece.x+i<width && 0<=piece.y+j &&  0<=piece.x+i) {
        if (board(piece.y + j)(piece.x + i) == 1 || piece.get(j, i) == 1) {
          copyMap(piece.y + j)(piece.x + i) = 1
        }
      }
    }
    board=copyMap
    clearRows
  }

  /**
    * Clear the rows that are fully occupied
    */
  def clearRows={
    val incompleteRows = board.filterNot(a => a.forall(el => el==1))
    board =  Array.ofDim[Int](height-incompleteRows.length, width) ++ incompleteRows
  }

  /**
    * First 4 rows are danger zone, if any of them becomes occupied, game over
    * @return
    */
  def isGameOver:Boolean={
    board.slice(0, 4).exists((row: Array[Int]) => row contains 1)
  }

  /**
    * Check if the piece is moved by yShift vertically and xShift horizontally has any collisions or outside of boundary
    * @param yShift
    * @param xShift
    * @return
    */
  def shiftAllowed(yShift:Int, xShift:Int): Boolean ={
    for (j <- 0 until piece.size; i <- 0 until piece.size){
      if(piece.get(j,i)==1){
        val newY = piece.y+j+yShift
        val newX = piece.x+i+xShift
        if (newX<0 || newX >= width)
          return false
        if (newY >= height)
          return false
        if(board(piece.y+j+yShift)(piece.x+i+xShift)==1){
          return false
        }
      }
    }
    true
  }

  //def stateAllowed(piece: Piece)

  /**
    * Move y vertically, x horizontally
    * @param yShift
    * @param xShift
    */
  def movePiece(yShift:Int, xShift:Int)={
    piece.yx_(piece.y + yShift, piece.x + xShift)
  }



  /**
    * If rotation is allowed, piece is rotated
    * @return
    */
  def rotate:Boolean={
    // Rotate piece only
    val rotatedPiece = piece.rotate
    // Check each cube of piece if it would collide with an existing cube or outside the boundary
    for (j <- 0 until rotatedPiece.size; i <- 0 until rotatedPiece.size){
      if(rotatedPiece.get(j,i)==1){
        val x = piece.x+i
        val y = piece.y+j
        if(y<0 || x<0 || y>=height ||x>=width || get(y, x)==1){
          return false
        }
      }
    }
    piece = rotatedPiece
    return true
  }

  /**
    *
    * @param y
    * @param x
    * @return 0 if empty, 1 if occupied, 2 if a moving piece
    */
  def get(y:Int, x:Int):Int={

    try {
      if ((piece.y until (piece.y + piece.size()) contains y) &&
        (piece.x until (piece.x + piece.size()) contains x)) {
        if (piece.get(y - piece.y, x - piece.x) == 1) {
          return 2
        }
      }
      board(y)(x)
    } catch{
      case e:Exception => e.printStackTrace(); return -1
    }

  }
}
