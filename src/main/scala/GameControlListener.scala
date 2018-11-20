/**
  * Created by oguz on 18.11.2018.
  */
trait GameControlListener {
  def down
  def left
  def right
  def drop
  def undo
  def rotate
  def newGame
  def exit = System.exit(0)
}
