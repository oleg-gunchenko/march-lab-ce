package ws.exon.cm.market.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.springframework.stereotype.Component;
import ws.exon.cm.market.utlis.DroolsUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroolsManagerService {

//    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
//    ListenableFuture<Explosion> explosion = service.submit(new Callable<Explosion>() {
//        public Explosion call() {
//            return pushBigRedButton();
//        }
//    });
//Futures.addCallback(explosion, new FutureCallback<Explosion>() {
//        // we want this handler to run immediately after we push the big red button!
//        public void onSuccess(Explosion explosion) {
//            walkAwayFrom(explosion);
//        }
//        public void onFailure(Throwable thrown) {
//            battleArchNemesis(); // escaped the explosion!
//        }
//    });

    static {
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
    }

    private final KieServices kieServices = KieServices.Factory.get();
    private final Map<ReleaseId, KieContainer> containers = new HashMap<>();

    public String errorMessages(final KieBuilder kb) {
        final StringBuilder aLog = new StringBuilder();

//        DrlParser parser = new DrlParser();
//        PackageDescr descr = parser.parse(true, drl);
//
//        if (parser.hasErrors()) {
//            for (DroolsError error : parser.getErrors()) {
//                aLog.append(error.getMessage() + "\n");
//            }
//        }

        if (kb.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            for (org.kie.api.builder.Message info : kb.getResults().getMessages())
                aLog.append(info.toString()).append(StringUtils.LF);
            return null;
        }
        return aLog.toString();
    }

    public void loadRules(final ReleaseId releaseId, final String root, final String... files) throws IOException {
        final ClassLoader cl = getClass().getClassLoader();
        for (final String file : files) {
            final String name = root + "/" + file;
            final String text = IOUtils.toString(Objects.requireNonNull(cl.getResourceAsStream(name)));
//            dms.uploadRule(releaseId, name, text);
        }
    }


    public ReleaseId createReleaseId(final String groupId, final String artifactId, String version) {
        return artifactId == null
                ? kieServices.getRepository().getDefaultReleaseId()
                : kieServices.newReleaseId(groupId, artifactId, version);
    }

    public ReleaseId createReleaseId(final String groupId, final String artifactId) {
        return createReleaseId(groupId, artifactId, "LATEST");
    }

    public KieContainer getKieContainer(final String groupId, final String artifactId) {
        final ReleaseId releaseId = createReleaseId(groupId, artifactId);
        return getKieContainer(releaseId);
    }

    public KieSession backTestSession(final String releaseId) {
        final String[] parts = releaseId.split(":");
        final ReleaseId rid = createReleaseId(parts[0], parts[1], parts[2]);
        final KieContainer container = getKieContainer(rid);
        return container.newKieSession(DroolsUtil.getBackTestSessionName(rid));
    }

    public KieContainer getKieContainer(final ReleaseId releaseId) {
        if (containers.containsKey(releaseId))
            return containers.get(releaseId);

        final KieRepository repo = kieServices.getRepository();
        final KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        DroolsUtil.scan(releaseId, template, (key, rule) -> DroolsUtil.addRule(kieFileSystem, releaseId, key, rule));
        kieFileSystem.generateAndWritePomXML(releaseId);

        final KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
        final KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(releaseId.getArtifactId().concat("KieBase"))
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.STREAM)
                .addPackage("*");

        kieBaseModel.newKieSessionModel(releaseId.getArtifactId().concat("RealTime"))
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.REALTIME);

        kieBaseModel.newKieSessionModel(releaseId.getArtifactId().concat("BackTest"))
                .setDefault(false)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.PSEUDO);

        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());

        final KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        final List<Message> messages = kieBuilder.getResults().getMessages(Message.Level.ERROR);
        if (!messages.isEmpty()) {
            for (final Message err : messages)
                System.err.println(err);
            return null;
        }

        repo.addKieModule(kieBuilder.getKieModule());

//        KieSessionConfiguration ksconf = KieServices.Factory.get().newKieSessionConfiguration();
//        ksconf.setOption( TimedRuleExecutionOption.YES );
//        KSession ksession =  ki.newKieSession(ksconf, null);
//        conf.setOption( new TimedRuleExecutionOption.FILTERED(new TimedRuleExecutionFilter() {
//            public boolean accept(Rule[] rules) {
//                return rules[0].getName().equals("MyRule");
//            }
//        }) );
        return kieServices.newKieContainer(releaseId);
    }


    public void addRule(final ReleaseId releaseId, final String name, final String text) {
    }

    public void deleteRule(final ReleaseId releaseId, final String name) {
    }

    public Object postDroolsVerifier() {
        StringBuilder aLog = new StringBuilder();

//        VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
//
//        Verifier verifier = vBuilder.newVerifier();
//
//        verifier.addResourcesToVerify(ResourceFactory.newByteArrayResource(iRequest.getData().getBytes()), ResourceType.DRL);
//
//        if (verifier.hasErrors()) {
//            for (VerifierError error : verifier.getErrors()) {
//                logger.debug(error.getMessage());
//                aLog.append(error.getMessage() + "\n");
//            }
//        } else {
//
//            verifier.fireAnalysis();
//
//            VerifierReport result = verifier.getResult();
//            for (VerifierMessageBase base : result.getBySeverity(Severity.WARNING)) {
//                logger.debug(base.toString());
//                aLog.append(base + "\n");
//            }
//        }
//        iRequest.setLog(aLog.toString());
//        logger.debug(iRequest.toString());
//
//        return iRequest;
        return null;
    }
}
