/**
  * Created by oguz on 31.10.2018.
  */
class GameLogic(val height:Int, val width:Int) {

  private var gameUI:GameUI=null

  /**
    * Contains the board and the current piece
    */
  val board = new GameArea(height, width)


  private val t = new java.util.Timer()

  private val task = new java.util.TimerTask {
    def run = tick
  }

  /**
    * Ran when 'a' is pressed, commands the piece to move left if possible
    */
  def left = if (board.shiftAllowed(0, -1)) board.movePiece(0, -1)

  /**
    * Ran when 'd is pressed, commands the piece to move right if possible
    */
  def right: Boolean = {
    if (board.shiftAllowed(0, 1)) {
      board.movePiece(0, 1)
      gameUI.updateCanvas(board)
      return true
    }
    else
      gameUI.updateCanvas(board)
      return false
  }

  /**
    * Ran when 's' is pressed and every timestep, commands the piece to move down if possible
    */
  def down: Boolean = {
    if (board.shiftAllowed(1, 0)) {
      board.movePiece(1, 0)
      gameUI.updateCanvas(board)
      true
    }
    else {
      board.fix
      pieceExists = false
      gameUI.updateCanvas(board)
      false
    }
  }


  /**
    * Ran when 'e' is pressed, commands the piece to rotate if possible
    */
  def rotate: Boolean = board.rotate

  var pieceExists=false

  /**
    * Main game loop content
    */
  def tick = {
    if(!pieceExists){
      board.newPiece
      pieceExists=true
    }
    else {down}
    if (board.isGameOver){
      println("GAME OVER!!!")
      t.cancel()
    }
    gameUI.updateCanvas(board)
  }


  /**
    * Trigger the game loop
    */
  def start = {
    new Thread(() => {
      t.schedule(task, 1000, 200)
    }).start
  }
}

/**
  * Run this to start the game
  */
object GameLogic{
  /**
    * Starts the game
    * @param args if given, first parameter is number of cubes vertically, second is number of cubes horizontally
    */
  def main(args: Array[String]): Unit = {
    val height = (if (args.length>0) args(0).toInt else 20) + 4
    val width = if (args.length>1) args(1).toInt else 10
    /**
      * Initialize the UI and game logic; couple them.
      */
    val gameLogic = new GameLogic(height, width)
    val gameUI = new GameUI(height, width)
    gameLogic.gameUI = gameUI
    gameUI.gameLogic = gameLogic
    gameUI.main(Array())
    /**
      * Start the game loop
      */
    gameLogic.start
  }
}

