package de.fzi.cep.sepa.esper.filter.text;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.fzi.cep.sepa.commons.Utils;
import de.fzi.cep.sepa.desc.EpDeclarer;
import de.fzi.cep.sepa.esper.config.EsperConfig;
import de.fzi.cep.sepa.esper.util.StringOperator;
import de.fzi.cep.sepa.model.impl.Domain;
import de.fzi.cep.sepa.model.impl.EventGrounding;
import de.fzi.cep.sepa.model.impl.eventproperty.EventProperty;
import de.fzi.cep.sepa.model.impl.eventproperty.EventPropertyPrimitive;
import de.fzi.cep.sepa.model.impl.EventSchema;
import de.fzi.cep.sepa.model.impl.EventStream;
import de.fzi.cep.sepa.model.impl.staticproperty.FreeTextStaticProperty;
import de.fzi.cep.sepa.model.impl.staticproperty.MappingPropertyUnary;
import de.fzi.cep.sepa.model.impl.staticproperty.OneOfStaticProperty;
import de.fzi.cep.sepa.model.impl.staticproperty.Option;
import de.fzi.cep.sepa.model.impl.staticproperty.StaticProperty;
import de.fzi.cep.sepa.model.impl.TransportFormat;
import de.fzi.cep.sepa.model.impl.graph.SepaDescription;
import de.fzi.cep.sepa.model.impl.graph.SepaInvocation;
import de.fzi.cep.sepa.model.impl.output.OutputStrategy;
import de.fzi.cep.sepa.model.impl.output.RenameOutputStrategy;
import de.fzi.cep.sepa.model.util.SepaUtils;
import de.fzi.cep.sepa.model.vocabulary.MessageFormat;
import de.fzi.cep.sepa.model.vocabulary.SO;
import de.fzi.cep.sepa.util.StandardTransportFormat;


public class TextFilterController extends EpDeclarer<TextFilterParameter> {
	
	@Override
	public SepaDescription declareModel() {
		
		List<String> domains = new ArrayList<String>();
		domains.add(Domain.DOMAIN_PERSONAL_ASSISTANT.toString());
		domains.add(Domain.DOMAIN_PROASENSE.toString());
		
		List<EventProperty> eventProperties = new ArrayList<EventProperty>();	
		EventProperty property = new EventPropertyPrimitive("name", "description", "a", de.fzi.cep.sepa.commons.Utils.createURI(SO.Text));
	
		eventProperties.add(property);
		
		EventSchema schema1 = new EventSchema();
		schema1.setEventProperties(eventProperties);
		
		EventStream stream1 = new EventStream();
		stream1.setEventSchema(schema1);
		
		SepaDescription desc = new SepaDescription("/sepa/textfilter", "Text Filter", "Text Filter Description", "", "/sepa/textfilter", domains);
		desc.setSupportedGrounding(StandardTransportFormat.getSupportedGrounding());
		
		desc.setIconUrl(EsperConfig.iconBaseUrl + "/Textual_Filter_Icon_HQ.png");
		
		//TODO check if needed
		stream1.setUri(EsperConfig.serverUrl +"/" +desc.getElementId());
		desc.addEventStream(stream1);
		List<OutputStrategy> strategies = new ArrayList<OutputStrategy>();
		strategies.add(new RenameOutputStrategy("Enrich", "EnrichedMovementAnalysis"));
		desc.setOutputStrategies(strategies);
		
		List<StaticProperty> staticProperties = new ArrayList<StaticProperty>();
		
		OneOfStaticProperty operation = new OneOfStaticProperty("operation", "Select Operation");
		operation.addOption(new Option("MATCHES"));
		operation.addOption(new Option("CONTAINS"));
		staticProperties.add(operation);
		try {
			staticProperties.add(new MappingPropertyUnary(new URI(property.getElementName()), "text", "Select Text Property"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		staticProperties.add(new FreeTextStaticProperty("keyword", "Select Keyword"));
		desc.setStaticProperties(staticProperties);
		
		return desc;
	}

	@Override
	public boolean invokeRuntime(SepaInvocation sepa) {
			
			String keyword = ((FreeTextStaticProperty) (SepaUtils
					.getStaticPropertyByName(sepa, "keyword"))).getValue();
			String operation = SepaUtils.getOneOfProperty(sepa,
					"operation");
			String filterProperty = SepaUtils.getMappingPropertyName(sepa,
					"text");
			
			logger.info("Text Property: " +filterProperty);
		
			TextFilterParameter staticParam = new TextFilterParameter(sepa, 
					keyword, 
					StringOperator.valueOf(operation), 
					filterProperty);
			
			
			try {
				
				return invokeEPRuntime(staticParam, TextFilter::new, sepa);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
	}
}
