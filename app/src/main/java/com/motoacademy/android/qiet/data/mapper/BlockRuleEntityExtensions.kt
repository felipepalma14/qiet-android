package com.motoacademy.android.qiet.data.mapper

import com.motoacademy.android.qiet.data.local.entity.BlockRuleEntity
import com.motoacademy.android.qiet.domain.model.BlockRule

fun BlockRuleEntity.toModel(): BlockRule {
    return BlockRule(
        id = this.id,
        ruleName = this.ruleName,
        isEnabled = this.isEnabled,
        color = this.color,
        blockedContacts = this.blockedContacts,
        blockedRegexRules = this.blockedRegexRules,
        interval = this.interval,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun BlockRule.toEntity(): BlockRuleEntity {
    return BlockRuleEntity(
        id = this.id,
        ruleName = this.ruleName,
        isEnabled = this.isEnabled,
        color = this.color,
        blockedContacts = this.blockedContacts,
        blockedRegexRules = this.blockedRegexRules,
        interval = this.interval,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

