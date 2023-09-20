package com.couple.diary.presentation.member

import com.couple.diary.application.member.MemberService
import com.couple.diary.application.member.dto.MemberLoginResponse
import com.couple.diary.application.member.dto.MemberReadResponse
import com.couple.diary.presentation.member.dto.MemberJoinRequest
import com.couple.diary.presentation.member.dto.MemberLoginRequest
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/join")
    fun join(@RequestBody @Validated request: MemberJoinRequest): ResponseEntity<Unit> {
        val memberNo = memberService.join(request.toParam())
        val uri = URI("/v1/members/$memberNo")
        return ResponseEntity.created(uri).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody request: MemberLoginRequest): ResponseEntity<MemberLoginResponse> =
        ResponseEntity.ok(
            memberService.login(request.toParam())
        )

    @GetMapping("/{memberNo}")
    fun readMember(@PathVariable memberNo: Long): ResponseEntity<MemberReadResponse> =
        ResponseEntity.ok(
            memberService.readMember(memberNo)
        )
}