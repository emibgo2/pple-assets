package link.pple.assets.domain.chat.entity

import link.pple.assets.configuration.jpa.BaseAuditingEntity
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

/**
 * @Author Heli
 */
@Entity
@Table(name = "chat_rooms")
class ChatRoom private constructor(

    var title: String,

    @Enumerated(EnumType.STRING)
    var status: Status
) : BaseAuditingEntity() {

    enum class Status {
        ACTIVE, DELETE
    }

    companion object {

        fun create(
            title: String
        ) = ChatRoom(
            title = title,
            status = Status.ACTIVE
        )
    }
}
