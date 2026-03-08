package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto

class UserMapper {
    fun toDomain(dto: UserDto) = User(
        id = dto.id,
        username = dto.username,
        email = dto.email,
        avatarUrl = dto.avatar_url,
    )

    fun toDto(domain: User) = UserDto(
        id = domain.id,
        username = domain.username,
        email = domain.email,
        avatar_url = domain.avatarUrl,
    )
}