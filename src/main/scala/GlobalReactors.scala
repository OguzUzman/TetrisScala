/**
  * Created by oguz on 18.11.2018.
  */
object GlobalReactors {
  private var _gameBoardListeners:List[GameBoardObserver]=List()
  private var _gameControlObservers:List[GameControlListener]=List()
  private var _gameBoardProvider:GameBoardProvider=_

  def gameBoardObservers = _gameBoardListeners
  def gameControlObservers = _gameControlObservers
  def gameBoardProvider = _gameBoardProvider

  def addGameBoardListener(gameBoardListener: GameBoardObserver) =
    _gameBoardListeners=gameBoardObservers:+gameBoardListener

  def addGameControlListener(gameControlListener: GameControlListener) =
    _gameControlObservers = gameControlObservers:+ gameControlListener

  def gameBoardProvider_(gameBoardProvider: GameBoardProvider)=_gameBoardProvider = gameBoardProvider

}
