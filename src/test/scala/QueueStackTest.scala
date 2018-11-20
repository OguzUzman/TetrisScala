import org.scalatest.FlatSpec

/**
  * Created by oguz on 17.11.2018.
  */
class QueueStackTest extends FlatSpec{
  val stack:QueueStack[Int] = new QueueStack[Int](3)
  stack.push(1)
  assert (stack.stack.sameElements(List(1)))
  stack.push(2)
  assert (stack.stack.sameElements(List(1, 2)))
  stack.push(3)
  assert (stack.stack.sameElements(List(1, 2, 3)))
  stack.push(4)
  assert (stack.stack.sameElements(List(2, 3, 4)))
  assert (stack.pop == 4)
  assert (stack.pop == 3)
  assert (stack.pop == 2)
  assertThrows[ArrayIndexOutOfBoundsException](stack.pop)

}
