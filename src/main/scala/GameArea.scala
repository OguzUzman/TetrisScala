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
    piece = RandomPieceGenerator.getNew()
    pieceLocation = (0, (width-piece.size())/2)
  }
  /**
    * Fixes the piece onto the board.
    */
  def fix={
    var copyMap = board.clone()
    for (j <- 0 until piece.size; i <- 0 until piece.size){
      if (pieceLocation._1+j<height && pieceLocation._2+i<width && 0<=pieceLocation._1+j &&  0<=pieceLocation._2+i) {
        if (board(pieceLocation._1 + j)(pieceLocation._2 + i) == 1 || piece.get(j, i) == 1) {
          copyMap(pieceLocation._1 + j)(pieceLocation._2 + i) = 1
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
        if(board(pieceLocation._1+j+yShift)(pieceLocation._2+i+xShift)==1){
          return false
        }
      }
    }
    true
  }

  def stateAllowed(piece: Piece, )

  /**
    * Move y vertically, x horizontally
    * @param y
    * @param x
    */
  def movePiece(y:Int,x:Int)={
    pieceLocation=(pieceLocation._1 + y, pieceLocation._2 + x):(Int, Int)
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
        if(pieceLocation._1+j<0 || pieceLocation._2+i<0 || pieceLocation._1+j>=height ||
          pieceLocation._2+i>=width || board(pieceLocation._1+j)(pieceLocation._2+i)==1){
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
      if ((pieceLocation._1 until (pieceLocation._1 + piece.size()) contains y) &&
        (pieceLocation._2 until (pieceLocation._2 + piece.size()) contains x)) {
        if (piece.get(y - pieceLocation._1, x - pieceLocation._2) == 1) {
          return 2
        }
      }
      board(y)(x)
    } catch{
      case e:Exception => e.printStackTrace(); return -1
    }

  }
}
