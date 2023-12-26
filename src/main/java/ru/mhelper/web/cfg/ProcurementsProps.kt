package ru.mhelper.web.cfg

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Данные о выводе закупок на веб странице
 */
@Component
@ConfigurationProperties(prefix = "procurements")
data class ProcurementsProps(val pageSize: Int = 20) {

}
