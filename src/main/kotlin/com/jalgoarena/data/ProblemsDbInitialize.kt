package com.jalgoarena.data

import com.jalgoarena.domain.Constants
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class ProblemsDbInitialize(
        setupProblemsStore: SetupProblemsDb = SetupProblemsXodusStore(Constants.storePath)
) : InitializingBean, SetupProblemsDb by setupProblemsStore {

    override fun afterPropertiesSet() {
        removeDb()
        createDb()
    }
}
