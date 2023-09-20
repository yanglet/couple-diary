package com.couple.diary.application.member.exception

import com.couple.diary.application.common.exception.BusinessException

class EmailDuplicatedException(message: String) : BusinessException(message) {
}