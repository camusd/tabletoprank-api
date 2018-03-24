package com.dylancamus.tabletoprank.util

class RestException(message: String, vararg val args: Any) : RuntimeException(message)