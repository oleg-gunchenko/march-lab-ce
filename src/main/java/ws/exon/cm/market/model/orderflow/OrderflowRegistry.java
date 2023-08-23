package ws.exon.cm.market.model.orderflow;

import com.google.common.reflect.ClassPath;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.model.orderflow.aggregator.meta.CandleFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderflowRegistry {
    private final ApplicationContext context;

    @PostConstruct
    public void initialize() throws IOException {
        for (final Object proxy : context.getBeansWithAnnotation(CandleFactory.class).values()) {
            final Class<?> cp = AopProxyUtils.ultimateTargetClass(proxy);
            final CandleFactory def = cp.getAnnotation(CandleFactory.class);
        }
        final Set<ClassPath.ClassInfo> factories = ClassPath.from(Thread.currentThread().getContextClassLoader())
                .getAllClasses().stream()
                .filter(ci -> ci.getClass().isAnnotationPresent(CandleFactory.class))
                .collect(Collectors.toSet());
        final ClassPath.ClassInfo ci = factories.iterator().next();
        ClassGraph cg = new ClassGraph();
        final ClassInfoList cil = cg.scan().getClassesWithAnnotation(CandleFactory.class.getCanonicalName());

    }
}
