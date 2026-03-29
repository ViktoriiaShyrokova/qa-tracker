package org.qatracker.service;

import org.qatracker.model.TestPriority;

public class PriorityRouter {

    private PriorityRouter() {
    }

    public static String route(TestPriority priority) {

      return switch (priority) {
            case SMOKE -> "Fast lane: run immediately";
          case SANITY -> "Standard lane: run in 5 min";
          case REGRESSION -> "Batch lane: run in next build";
          case PERFORMANCE -> "Scheduled lane: run nightly";
        };
    }
    public static int estimateMinutes(TestPriority priority) {
        return switch (priority) {
            case SMOKE -> 0;
            case SANITY -> 5;
            case REGRESSION -> { yield 30 * priority.getLevel(); }
            case PERFORMANCE -> 480;
        };
    }
}
