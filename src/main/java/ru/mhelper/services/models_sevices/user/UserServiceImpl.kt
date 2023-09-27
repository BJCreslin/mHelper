package ru.mhelper.services.models_sevices.user

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mhelper.controllers.exeptions.BadRequestException
import ru.mhelper.models.BaseStatus
import ru.mhelper.models.users.ERole
import ru.mhelper.models.users.User
import ru.mhelper.repository.UserRepository
import ru.mhelper.services.models_sevices.role.RoleService

const val TELEGRAM_ID_IS_NULL = "telegramId is null"
const val USER_NOT_FOUND = "User not found"
const val USER_IS_NULL = "User is null"
const val USERNAME_IS_NULL = "UserName is null"
const val EMAIL_IS_NULL = "UserName is null"
const val USER_WITH_NAME_NOT_FOUND = "User with name %s not found"

@Service
open class UserServiceImpl(private val repository: UserRepository, private val roleService: RoleService) :
    UserService {

    /**
     * Creates new telegram user with roles [ERole.ROLE_TELEGRAM] and [ERole.CHROME_EXTENSION]
     */
    @Transactional
    override fun createNewTelegramUser(telegramId: Long?): User {
        if (telegramId == null) {
            throw BadRequestException(TELEGRAM_ID_IS_NULL)
        }
        val user = getNewTelegramUser(telegramId)
        return repository.save(user)
    }

    /**
     * Check exist User with telegramId
     */
    @Transactional(readOnly = true)
    override fun existsByTelegramUserId(telegramId: Long?): Boolean {
        if (telegramId == null) {
            throw BadRequestException(TELEGRAM_ID_IS_NULL)
        }
        return repository.existsByTelegramUserId(telegramId)
    }

    /**
     * Check exist User with userName
     */
    @Transactional(readOnly = true)
    override fun existsByUsername(userName: String?): Boolean {
        if (userName == null) {
            throw BadRequestException(USERNAME_IS_NULL)
        }
        return repository.existsByUsername(userName)
    }

    /**
     * Check exist User with email
     */
    @Transactional(readOnly = true)
    override fun existsByEmail(email: String?): Boolean {
        if (email == null) {
            throw BadRequestException(EMAIL_IS_NULL)
        }
        return repository.existsByEmail(email)
    }

    /**
     * Check exist User with telegramId
     */
    @Transactional(readOnly = true)
    override fun getUserByTelegramId(telegramId: Long?): User {
        if (telegramId == null) {
            throw BadRequestException(TELEGRAM_ID_IS_NULL)
        }
        val user = repository.findByTelegramUserId(telegramId).orElseThrow {
            BadRequestException(USER_NOT_FOUND)
        }
        return user
    }

    /**
     * Get exist User or new
     */
    @Transactional
    override fun getOrNewUserByTelegramId(telegramId: Long?): User {
        if (telegramId == null) {
            throw BadRequestException(TELEGRAM_ID_IS_NULL)
        }
        val userOptional = repository.findByTelegramUserId(telegramId)
        return if (userOptional.isPresent) {
            userOptional.get()
        } else {
            createNewTelegramUser(telegramId)
        }
    }

    /**
     * get User by UserName
     */
    @Transactional(readOnly = true)
    override fun getUserByUserName(userName: String?): User {
        if (userName == null) {
            throw BadRequestException(USERNAME_IS_NULL)
        }
        val user = repository.findByUsername(userName).orElseThrow {
            UsernameNotFoundException(
                String.format(
                    USER_WITH_NAME_NOT_FOUND,
                    userName
                )
            )
        }
        return user
    }

    /**
     * Save User to DB
     */
    @Transactional
    override fun save(user: User?): User {
        if (user == null) {
            throw BadRequestException(USER_IS_NULL)
        }
        return repository.save(user)
    }

    override fun isUserCorrect(user: User?): CheckUserResult {
        return when {
            user == null || user.id == null || !user.isEnabled || user.status in arrayOf(
                BaseStatus.BANNED,
                BaseStatus.DELETED,
                BaseStatus.NOT_ACTIVE
            ) -> CheckUserResult.INCORRECTED

            else -> CheckUserResult.CORRECTED
        }
    }

    private fun getNewTelegramUser(telegramId: Long): User {
        return User(
            telegramId,
            User.PREFIX_TELEGRAM_NAME + telegramId,
            User.TELEGRAM_DB_PASSWORD,
            telegramId.toString() + User.POSTFIX_TELEGRAM_EMAIL,
            true,
            BaseStatus.ACTIVE,
            getRolesForNewTelegramUser()
        )
    }

    private fun getRolesForNewTelegramUser() = setOf(
        roleService.getRoleByERole(ERole.ROLE_TELEGRAM),
        roleService.getRoleByERole(ERole.CHROME_EXTENSION)
    )
}