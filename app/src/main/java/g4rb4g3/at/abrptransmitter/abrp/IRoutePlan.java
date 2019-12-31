package g4rb4g3.at.abrptransmitter.abrp;

import g4rb4g3.at.abrptransmitter.gson.abrp.GsonRoutePlan;

public interface IRoutePlan {
  void planReady(GsonRoutePlan route);

  void planFailed();
}
