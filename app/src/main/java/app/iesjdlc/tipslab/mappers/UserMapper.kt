package app.iesjdlc.tipslab.mappers

import app.iesjdlc.tipslab.models.domain.ProviderType
import app.iesjdlc.tipslab.models.domain.User
import app.iesjdlc.tipslab.models.dto.UserDto

class UserMapper {
    fun toDomain(dto: UserDto) = User(
        id = dto.id,
        email = dto.email,
        username = dto.username,
        photoUrl = dto.photo_url,
        provider = ProviderType.valueOf(dto.provider),
        favorites = dto.favorites
    )

    fun toDto(domain: User) = UserDto(
        id = domain.id,
        email = domain.email,
        username = domain.username,
        photo_url = domain.photoUrl,
        provider = domain.provider.name,
        favorites = domain.favorites
    )
}