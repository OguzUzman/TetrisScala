/**
  * Created by oguz on 31.10.2018.
  */
class GameController(val height:Int, val width:Int) extends GameControlListener{


  private var board:GameBoard = _

  private var pieceExists = false

  private var t:java.util.Timer = _

  private var task:java.util.TimerTask = _

  initBoard()


  def initBoard()={
    board = new GameBoard(height, width)
    pieceExists = false
    t = new java.util.Timer()
    task = new java.util.TimerTask {def run = tick}
  }


  /**
    * Main game loop contentm
    */
  def tick = {
    if(!board.pieceExists) board.newRandomPiece else down
    if (board.isGameOver){
      println("GAME OVER!!!")
      t.cancel()
    }
  }


  /**
    * Trigger the game loop
    */
  def start = {
    new Thread(() => {
      t.schedule(task, 1000, 200)
    }).start
  }

  def newGame ={
    t.cancel()
    task.cancel()
    initBoard()
    start
  }


  override def down: Unit = board.down

  override def left: Unit = board.left

  override def right: Unit = board.right

  override def drop: Unit = board.drop

  override def undo: Unit = board.undo

  override def rotate: Unit = board.rotate

}

/**
  * Run this to start the game
  */
object GameController{
  /**
    * Starts the game
    * @param args if given, first parameter is number of cubes vertically, second is number of cubes horizontally
    */
  def main(args: Array[String]): Unit = {
    val height = (if (args.length>0) args(0).toInt else 20) + 4
    val width = if (args.length>1) args(1).toInt else 10
    start(width, height)
  }

  def start(width:Int, height:Int):(GameController, GameUI)={
    /**
      * Initialize the UI and game logic; couple them.
      */
    val gameController = new GameController(height, width)
    val gameUI = new GameUI(height, width)
    gameUI.main(Array())
    /**
      * Start the game loop
      */
    gameController.start
    return (gameController, gameUI)
  }
}

