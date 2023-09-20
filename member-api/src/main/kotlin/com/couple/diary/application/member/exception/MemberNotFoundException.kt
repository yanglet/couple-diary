package com.couple.diary.application.member.exception

import com.couple.diary.application.common.exception.BusinessException

class MemberNotFoundException(message: String) : BusinessException(message) {
}