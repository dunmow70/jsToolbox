package com.karateca.jstoolbox.torelated;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class SwitchToViewAction extends GoToRelatedAction {

  @Override
  String getDestinationSuffix() {
    return viewSuffix;
  }
}
