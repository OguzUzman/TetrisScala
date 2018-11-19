
/**
  * Created by oguz on 17.11.2018.
  * Has a fixed size, on push, if the stack is full, oldest is removed
  * On pop, the most recent one is removed and returned
  */
class QueueStack[StackClass](val maxSize:Int) {

  var stack:List[StackClass] = List()

  def push(obj:StackClass)  ={
    if (stack.length == maxSize) discardHead
    stack = stack :+ obj
  }

  private def discardHead : Unit = stack = stack.drop(1)

  private def discardLast : Unit = stack = stack.dropRight(1)

  def pop:StackClass = {
    if (stack.nonEmpty) {
      val last: StackClass = stack.last
      discardLast
      last
    } else {
      throw new ArrayIndexOutOfBoundsException("The stack is empty.")
    }
  }

  def isEmpty = stack.isEmpty

  def flush = {stack = List()}
}
