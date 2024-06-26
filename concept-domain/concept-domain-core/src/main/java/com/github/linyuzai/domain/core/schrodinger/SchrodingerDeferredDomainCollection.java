package com.github.linyuzai.domain.core.schrodinger;

import com.github.linyuzai.domain.core.DomainCollection;
import com.github.linyuzai.domain.core.DomainObject;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * 薛定谔的集合模型
 */
@Getter
@Setter
public class SchrodingerDeferredDomainCollection<T extends DomainObject>
        extends AbstractSchrodingerDomainCollection<T> implements DomainCollection<T> {

    protected Supplier<DomainCollection<T>> supplier;

    @Override
    protected DomainCollection<T> doGetTarget() {
       return supplier.get();
    }

    @Override
    protected void onRelease() {
        supplier = null;
    }
}
