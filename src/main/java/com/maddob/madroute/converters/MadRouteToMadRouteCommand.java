package com.maddob.madroute.converters;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.NMEAParser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MadRouteToMadRouteCommand implements Converter<MadRoute, MadRouteCommand> {

    @Override
    public MadRouteCommand convert(MadRoute madRoute) {
        MadRouteCommand command = new MadRouteCommand();
        command.setId(madRoute.getId());
        command.setDescription(madRoute.getDescription());
        command.setLocation(madRoute.getLocation());
        command.setName(madRoute.getName());
        command.setVideoId(madRoute.getVideoId());
        return command;
    }
}
