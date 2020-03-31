package guru.mikelue.misc.hibernate.env;

/**
 * Static method to set logging of jBoss to SLF4j.
 */
public interface Slf4jConfigurer {
	public final static String JBOSS_LOGGING_PROVIDER = "org.jboss.logging.provider";

	/**
	 * You should call this method from static initializer of Main class.
	 */
	public static void jbossLoggingToSlf4j()
	{
		System.setProperty(JBOSS_LOGGING_PROVIDER, "slf4j");
	}
}
