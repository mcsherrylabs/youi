package io.youi.communication

import com.outr.reactify.Var
import io.youi.http.{Connection, ConnectionManager, WebSocketUtil}
import io.youi.net.URL
import org.scalajs.dom.WebSocket

abstract class ClientWebSocketCommunicator[C <: Communication](socketURL: URL, manager: ConnectionManager) extends Connection(manager) {
  val webSocket: Var[Option[WebSocket]] = Var(None)

  lazy val communication: C = create()

  protected def create(): C

  def connect(): Unit = synchronized {
    disconnect()
    webSocket := Some(WebSocketUtil.connect(socketURL, this))
  }

  def disconnect(): Unit = synchronized {
    webSocket().foreach { ws =>
      if (ws.readyState == WebSocket.OPEN) {
        ws.close()
      }
      webSocket := None
    }
  }

  override def close(): Unit = {
    super.close()

    disconnect()
  }
}