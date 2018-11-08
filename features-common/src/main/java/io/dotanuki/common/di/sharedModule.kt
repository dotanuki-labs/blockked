package io.dotanuki.common.di

import io.dotanuki.services.mesh.di.meshModule
import org.kodein.di.Kodein

val sharedModule = Kodein.Module("shared") {

    importOnce(meshModule)

}