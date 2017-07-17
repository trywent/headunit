package ca.yyx.hu.aap.protocol.messages

import ca.yyx.hu.aap.AapMessage
import ca.yyx.hu.aap.protocol.MsgType
import ca.yyx.hu.aap.protocol.nano.Media
import com.google.protobuf.nano.MessageNano

/**
 * @author algavris
 * *
 * @date 13/02/2017.
 */

class MediaAck(channel: Int, sessionId: Int)
    : AapMessage(channel, Media.MSG_MEDIA_ACK, MediaAck.makeProto(sessionId), MediaAck.ackBuf) {
    companion object {

        private val mediaAck = Media.Ack()
        private val ackBuf = ByteArray(20)

        private fun makeProto(sessionId: Int): MessageNano {
            mediaAck.clear()
            mediaAck.sessionId = sessionId
            mediaAck.ack = 1

            return mediaAck
        }
    }
}
