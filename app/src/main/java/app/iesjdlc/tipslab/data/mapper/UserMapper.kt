package app.iesjdlc.tipslab.data.mapper

import app.iesjdlc.tipslab.domain.model.ProviderType
import app.iesjdlc.tipslab.domain.model.User
import app.iesjdlc.tipslab.data.model.UserDto

class UserMapper {
    fun toDomain(dto: UserDto) = User(
        id = dto.id,
        email = dto.email,
        username = dto.username,
        photoUrl = dto.photo_url,
        provider = ProviderType.valueOf(dto.provider)
    )

    fun toDto(domain: User) = UserDto(
        id = domain.id,
        email = domain.email,
        username = domain.username,
        photo_url = domain.photoUrl,
        provider = domain.provider.name
    )
}