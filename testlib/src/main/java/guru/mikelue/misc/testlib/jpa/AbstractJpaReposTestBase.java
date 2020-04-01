package guru.mikelue.misc.testlib.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import guru.mikelue.misc.testlib.AbstractTestBase;

@Transactional
public abstract class AbstractJpaReposTestBase extends AbstractTestBase {
	@PersistenceContext
	private EntityManager em;

	protected AbstractJpaReposTestBase() {}

	public EntityManager getEntityManager()
	{
		return em;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(final EntityManager em) {
		this.em = em;
	}
}
