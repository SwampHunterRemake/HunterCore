package kr.cosine.swamp.hunter.mod.core.common.scannable

import io.github.classgraph.AnnotationEnumValue
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import kotlin.reflect.KClass

abstract class Scannable<T : Any>(
    protected val clazz: Class<T>,
    val priority: Int = 100
) {
    constructor(clazz: KClass<T>, priority: Int = 100) : this(clazz.java, priority)

    abstract fun handle(classInfo: ClassInfo)

    companion object {
        fun register() {
            ClassGraph()
                .overrideClassLoaders(Thread.currentThread().contextClassLoader)
                .acceptPackages("kr.cosine.swamp.hunter")
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()
                .use { scanResult ->
                    scanResult
                        .getSubclasses(Scannable::class.java)
                        .filter { !it.isAbstract && !it.isInterface && isCurrentEnvironment(it) }
                        .mapNotNull { it.loadClass().kotlin.objectInstance as? Scannable<*> }
                        .sortedBy { it.priority }
                        .forEach { scannable ->
                            if (scannable.clazz.isInterface) {
                                scanResult.getClassesImplementing(scannable.clazz)
                            } else {
                                scanResult.getSubclasses(scannable.clazz)
                            }.filter { !it.isAbstract && !it.isInterface && isCurrentEnvironment(it)  }
                                .forEach(scannable::handle)
                        }
                }
        }

        private fun isCurrentEnvironment(classInfo: ClassInfo): Boolean {
            val annotation = classInfo.getAnnotationInfo(Environment::class.java.name) ?: return true
            val value = annotation.parameterValues.getValue("value") as? AnnotationEnumValue ?: return true
            return value.valueName == FabricLoader.getInstance().environmentType.name
        }
    }
}