/**
  * Created by oguz on 19.11.2018.
  */
class GameBoard(val height: Int, val width:Int) extends GameBoardProvider{

  private final val UNDO_COUNT : Int = 3

  var board: Array[Array[Int]] = Array.ofDim(height, width)
  var _piece: Piece = _
  var pieceExists: Boolean = false

  val stateStack  = new QueueStack[GameState](UNDO_COUNT)
  GlobalReactors.gameBoardProvider_(this)

  // Getter
  def piece = _piece

  // Setter
  def piece_= (newPiece:Piece):Unit = {_piece = newPiece; pieceExists=true}

  /**
    * Random piece at the top of the board horizontally center
    */
  def newRandomPiece: Unit = {
    piece = PieceGenerator.getRandom().yx_(0, (width - 2) / 2)
    pieceExists = true
  }


  /**
    * Triggered on 'q'
    */
  def undo={
    if (stateStack.nonEmpty && !isGameOver) {
      board = stateStack.pop._board
      pieceExists = false
      gameBoardChanged
    }
  }


  /**
    * Fixes the piece onto the board.
    */
  def fix() = {
    stateStack.push(new GameState(board.map(_.clone)))
    var copyMap = board.map(_.clone)
    for (j <- 0 until piece.size; i <- 0 until piece.size) {
      if (piece.y + j < height && piece.x + i < width && 0 <= piece.y + j && 0 <= piece.x + i) {
        if (board(piece.y + j)(piece.x + i) == 1 || piece.get(j, i) == 1) {
          copyMap(piece.y + j)(piece.x + i) = 1
        }
      }
    }
    board = copyMap
    clearRows
    pieceExists = false
    gameBoardChanged
  }

  /**
    * Clear the rows that are fully occupied
    */
  def clearRows():Unit = {
    val incompleteRows = board.filterNot(a => a.forall(el => el == 1))
    board = Array.ofDim[Int](height - incompleteRows.length, width) ++ incompleteRows
  }

  /**
    * First 4 rows are danger zone, if any of them becomes occupied, game over
    *
    * @return
    */
  def isGameOver: Boolean = {
    board.slice(0, 4).exists((row: Array[Int]) => row contains 1)
  }

  /**
    * Check if the piece is moved by yShift vertically and xShift horizontally has any collisions or outside of boundary
    *
    * @param yShift
    * @param xShift
    * @return
    */
  def shiftAllowed(yShift: Int, xShift: Int): Boolean = {
    val movedPiece = piece.clone().yx_(piece.y+yShift, piece.x+xShift)
    pieceAllowed(movedPiece)
  }


  /**
    * If rotation is allowed, piece is rotated
    *
    * @return
    */
  def rotate(): Unit = {
    // Rotate piece only
    val rotatedPiece = piece.rotate
    if (pieceAllowed(rotatedPiece)) {piece = rotatedPiece; gameBoardChanged}
  }

  /**
    * Part of GameBoardProvider
    *
    * @param y
    * @param x
    * @return 0 if empty, 1 if occupied, 2 if a moving piece
    */
  override def getCell(y: Int, x: Int): Int = {
    if (pieceExists)
      getCell(y, x, piece)
    else
      board(y)(x)
  }

  /**
    * What would the cell value be if a generic @param pieceToTest was placed,
    *   0 for empty, 1 for fixed cell, 2 for piece cell
    * @param y
    * @param x
    * @param pieceToTest
    * @return
    */
  def getCell(y: Int, x: Int, pieceToTest:Piece):Int={
    if (board(y)(x) == 1)
      return 1
    if (pieceToTest==null)
      board(y)(x)
    if ((pieceToTest.y until (pieceToTest.y + pieceToTest.size()) contains y) &&
      (pieceToTest.x until (pieceToTest.x + pieceToTest.size()) contains x)) {
      if (pieceToTest.get(y - pieceToTest.y, x - pieceToTest.x) == 1) {
        return 2
      }
    }
    return 0
  }


  /**
    * Check if a hypothetical piece at certain state and position would be legal
    * @param pieceHypothesis
    * @return
    */
  def pieceAllowed(pieceHypothesis: Piece):Boolean={
    for (j <- 0 until pieceHypothesis.size; i <- 0 until pieceHypothesis.size) {
      if (pieceHypothesis.get(j, i) == 1) {
        val x = pieceHypothesis.x + i
        val y = pieceHypothesis.y + j
        if (y < 0 || x < 0 || y >= height || x >= width || getCell(y, x, pieceHypothesis) == 1) {
          return false
        }
      }
    }
    return true
  }

  /**
    * Ran when 'a' is pressed, commands the piece to move left if possible
    */
  def left():Unit = if (shiftAllowed(0, -1)) movePiece(0, -1)

  /**
    * Ran when 'd is pressed, commands the piece to move right if possible
    */
  def right():Unit = if (shiftAllowed(0, 1)) movePiece(0, 1)


  /**
    * Ran when 's' is pressed and every timestep, commands the piece to move down if possible
    */
  def down(): Unit= if (shiftAllowed(1, 0)) movePiece(1, 0) else fix

  def drop():Unit = {
    while (shiftAllowed(1, 0)){
      movePiece(1, 0)
    }
    fix
    pieceExists = false
  }


  /**
    * Called when any change on board is made, inform all observers
    * @return
    */
  def gameBoardChanged:AnyVal =
    GlobalReactors.gameBoardObservers.foreach((observer: GameBoardObserver) => observer.onGameBoardUpdate(this))


  /**
    * Move y vertically, x horizontally
    * @param yShift
    * @param xShift
    */
  def movePiece(yShift:Int, xShift:Int)={
    piece.yx_(piece.y + yShift, piece.x + xShift)
    gameBoardChanged
  }
}
