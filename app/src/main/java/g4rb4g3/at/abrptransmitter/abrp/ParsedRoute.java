package g4rb4g3.at.abrptransmitter.abrp;

import java.util.List;

import g4rb4g3.at.abrptransmitter.gson.abrp.GsonRoutePlan;
import g4rb4g3.at.abrptransmitter.gson.abrp.Route;
import g4rb4g3.at.abrptransmitter.gson.abrp.Step;

public class ParsedRoute {

  private final GsonRoutePlan plan;

  public ParsedRoute(GsonRoutePlan plan) {
    this.plan = plan;
  }

  public List<Route> getRoutes() {
    return plan.getResult().getRoutes();
  }

  public Route getFirstRoute() {
    return plan.getResult().getRoutes().get(0);
  }

  /**
   * returns steps of first route
   */
  public List<Step> getSteps() {
    return getFirstRoute().getSteps();
  }
}
