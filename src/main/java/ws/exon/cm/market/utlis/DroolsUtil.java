package ws.exon.cm.market.utlis;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.decisiontable.DecisionTableProviderImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DroolsUtil {
    public static void delRule(String packageName, String ruleName, KieBase kieBase) {
        try {
            kieBase.removeRule(packageName, ruleName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void addRules(String content, KieBase kieBase) {
        try {
            final KnowledgeBaseImpl knowledgeBase = (KnowledgeBaseImpl) kieBase;
            final KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            kb.add(ResourceFactory.newByteArrayResource(content.getBytes(StandardCharsets.UTF_8)), ResourceType.DRL);
            if (kb.hasErrors())
                log.info("ADD RULE FILE ERROR: " + kb.getErrors().toString());
            knowledgeBase.addPackages(kb.getKnowledgePackages());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void addRule(final KieFileSystem kieFileSystem, final ReleaseId releaseId, final String name, byte[] fileArray) {
        final Resource resource = ResourceFactory.newByteArrayResource(fileArray);
        resource.setSourcePath(formatKeyPath(releaseId, name));
        resource.setResourceType(ResourceType.DRL);
        kieFileSystem.write(resource);
    }

    public static void addRule(final KieFileSystem kieFileSystem, final ReleaseId releaseId, final String name, final String rule) {
        addRule(kieFileSystem, releaseId, name, rule.getBytes(StandardCharsets.UTF_8));
    }

    public static String getSessionName(final ReleaseId releaseId, final String suffix) {
        return releaseId.getArtifactId().concat(suffix);
    }

    public static String getBackTestSessionName(final ReleaseId releaseId) {
        return getSessionName(releaseId, "BackTest");
    }

    public static String getRealTimeSessionName(final ReleaseId releaseId) {
        return getSessionName(releaseId, "RealTime");
    }

    public static String formatKeyPath(final ReleaseId releaseId, final String name) {
        return String.format("%s/%s/%s-%s", releaseId.getGroupId(), releaseId.getArtifactId(), name, releaseId.getVersion());
    }

    public static String formatKeyPrefix(final ReleaseId releaseId) {
        return String.format("drools://%s:%s/%s", releaseId.getGroupId(), releaseId.getArtifactId(), releaseId.getVersion());
    }

    public static String formatKey(final ReleaseId releaseId, final String ruleName) {
        return String.format("%s#%s", formatKeyPrefix(releaseId), ruleName);
    }

    public static String formatScanKey(final ReleaseId release) {
        return formatKey(release, "*");
    }

    private void addClasspathResource(final KieFileSystem kieFileSystem, final String path) {
        kieFileSystem.write(ResourceFactory.newClassPathResource(path)); // "rules/FDInterestRate.xls"
    }

    public String getDrlFromExcel(String excelFile) {
        final DecisionTableConfiguration configuration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        configuration.setInputType(DecisionTableInputType.XLS);

        final org.kie.api.io.Resource dt = ResourceFactory.newClassPathResource(excelFile, getClass());
        final DecisionTableProviderImpl decisionTableProvider = new DecisionTableProviderImpl();

        return decisionTableProvider.loadFromResource(dt, null);
    }

    public interface ScanResult {
        void execute(final String key, final String rule);
    }

}
