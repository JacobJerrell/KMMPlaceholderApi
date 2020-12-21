package com.bocaj.kmmplaceholder.shared


import platform.UIKit.UIDevice

actual interface KMMDateTime <T> {
    actual val value: T
}
actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}