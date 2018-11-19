/**
  * Created by oguz on 19.11.2018.
  */
class GameBoard(val height: Int, val width:Int) extends GameBoardProvider{

  private final val UNDO_COUNT : Int = 3

  var board: Array[Array[Int]] = Array.ofDim(height + 4, width)
  var _piece: Piece = _
  var pieceExists: Boolean = false

  val stateStack  = new QueueStack[GameState](UNDO_COUNT)

  /**
    * Random piece at the top of the board horizontally center
    */
  def newRandomPiece: Unit = {
    _piece = PieceGenerator.getRandom().yx_(0, (width - 2) / 2)
    pieceExists = true
  }


  /**
    * Triggered on 'q'
    */
  def undo={
    board = stateStack.pop._board
    pieceExists = false
    gameBoardChanged
  }

  /**
    * Fixes the piece onto the board.
    */
  private def fix() = {
    stateStack.push(new GameState(board.map(_.clone)))
    var copyMap = board.map(_.clone)
    for (j <- 0 until _piece.size; i <- 0 until _piece.size) {
      if (_piece.y + j < height && _piece.x + i < width && 0 <= _piece.y + j && 0 <= _piece.x + i) {
        if (board(_piece.y + j)(_piece.x + i) == 1 || _piece.get(j, i) == 1) {
          copyMap(_piece.y + j)(_piece.x + i) = 1
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
    val movedPiece = _piece.clone().yx_(_piece.y+yShift, _piece.x+xShift)
    pieceAllowed(movedPiece)
  }


  /**
    * If rotation is allowed, piece is rotated
    *
    * @return
    */
  def rotate(): Unit = {
    // Rotate piece only
    val rotatedPiece = _piece.rotate
    if (pieceAllowed(rotatedPiece)) {_piece = rotatedPiece; gameBoardChanged}
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
      getCell(y, x, _piece)
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
    if (pieceToTest==null)
      board(y)(x)
    if ((pieceToTest.y until (pieceToTest.y + pieceToTest.size()) contains y) &&
      (pieceToTest.x until (pieceToTest.x + pieceToTest.size()) contains x)) {
      if (pieceToTest.get(y - pieceToTest.y, x - pieceToTest.x) == 1) {
        return 2
      }
    }
    board(y)(x)
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
          false
        }
      }
    }
    true
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
    GlobalReactors.gameBoardObservers.foreach((observer: GameBoardObserver) => observer.onUpdate(this))


  /**
    * Move y vertically, x horizontally
    * @param yShift
    * @param xShift
    */
  def movePiece(yShift:Int, xShift:Int)={
    _piece.yx_(_piece.y + yShift, _piece.x + xShift)
    gameBoardChanged
  }
}
