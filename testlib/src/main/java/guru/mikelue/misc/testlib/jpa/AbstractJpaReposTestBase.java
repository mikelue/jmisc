package guru.mikelue.misc.testlib.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import guru.mikelue.misc.testlib.AbstractTestBase;

/**
 * Depending on Springframework, this base class provides
 * {@link #getEntityManager()} and {@link #getDataSource()} to provide
 * convenient method for testing.
 */
@Transactional
public abstract class AbstractJpaReposTestBase extends AbstractTestBase {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DataSource ds;

	protected AbstractJpaReposTestBase() {}

	/**
	 * Retrieves the {@link EntityManager} under current application context.
	 */
	public EntityManager getEntityManager()
	{
		return em;
	}

	/**
	 * Retrieves the {@link DataSource} under current application context.
	 */
	public DataSource getDataSource()
	{
		return ds;
	}
}
