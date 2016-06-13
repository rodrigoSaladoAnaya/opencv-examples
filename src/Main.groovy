import org.opencv.core.Core;

class Main {
  static {
    Runtime.getRuntime().loadLibrary0(
      groovy.lang.GroovyClassLoader.class, Core.NATIVE_LIBRARY_NAME
    )
  }

  static main(args) {
    new app.Server(8010)
  }
}
