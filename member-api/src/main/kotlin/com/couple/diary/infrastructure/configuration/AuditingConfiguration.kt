package com.couple.diary.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
class AuditingConfiguration {
}