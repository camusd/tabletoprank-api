package com.dylancamus.tabletoprank.util

abstract class ApiSubError

data class ApiValidationError(
        val obj: String, val message: String) : ApiSubError() {

    private var field: String? = null
    private var rejectedField: Any? = null

    constructor(obj: String, field: String,
                rejectedField: Any, message: String) : this(obj, message) {
        this.field = field
        this.rejectedField = rejectedField
    }
}
