package com.couple.diary.application.security.exception

import com.couple.diary.application.common.exception.BusinessException

class TokenInvalidException(message: String) : BusinessException(message) {
}