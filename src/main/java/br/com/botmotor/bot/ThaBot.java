package br.com.botmotor.bot;

import br.com.botmotor.model.Place;
import br.com.botmotor.service.PlaceService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by luan on 3/19/16.
 */
public class ThaBot {

    public static final String BOT_NAME = "@botmoter_bot";
    private final Map<Long, UserSession> dados = new HashMap<>();

    public List<Response> r(String s) {
        return Arrays.asList(new Response(s));
    }

    public List<Response> process(Message m) {
        if (m instanceof MessageText) {
            MessageText messageText = (MessageText) m;
            if (messageText.getText() == null) {
                return null;
            }
            if (!messageText.getText().startsWith("/")) {
                return null;
            }
            if (messageText.getText().endsWith(BOT_NAME)) {
                messageText.setText(messageText.getText().substring(0,
                        messageText.getText().length() - BOT_NAME.length()));
            }
            System.out.println(messageText.getText());

            if ("/sim".equals(messageText.getText())){
                UserSession user = new UserSession();
                user.setEtapa(Etapa.ESCOLHA_LOCAL);
                user.setLocation(new MessageLocation(-22.893669, -47.04728, m.getUserId()));
                user.setTipoLocalEscolhido(TipoLocal.CAFES);

                List<Place> places = new PlaceService().getPlaces(user.getLatitude(), user.getLongitude(), user.getTipoLocalEscolhido());
                user.setPlaces(places);
                dados.put(m.getUserId(), user);
                AtomicInteger a = new AtomicInteger(0);
                return r("O que você acha desse locais que encontrei: \n" + places.stream().map(l -> "/opcao" + a.incrementAndGet() + " " + l.toLine()).collect(Collectors.joining("\n")));


            }

            if ("/start".equals(messageText.getText())) {

                String retorno;

                if (dados.containsKey(messageText.getUserId())) {
                    retorno = "E agora, o que você procura?";
                } else {
                    retorno = "E ai, o que você está procurando?";
                }
                dados.put(messageText.getUserId(), new UserSession());

                return r(retorno + "\n" +
                        "Digite /restaurantes para buscarmos restaurantes \n" +
                        "Digite /cafes para buscarmos Cafés \n" +
                        "Digite /bares para buscarmos bares");
            }

            if (!dados.containsKey(messageText.getUserId())) {
                return r("Digite /start para comerçarmos");
            }

            UserSession sessao = dados.get(messageText.getUserId());

            System.out.println("------------------------" + sessao.getEtapa());
            if (sessao.getEtapa() == Etapa.ESCOLHA_TIPO_LOCAL) {
                if (!Pattern.matches("/.*", messageText.getText())) {
                    return null;
                }

                String valor = messageText.getText().substring(1);

                TipoLocal tipoLocal;
                try {
                    tipoLocal = TipoLocal.valueOf(valor.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return null;
                }

                sessao.setTipoLocalEscolhido(tipoLocal);
                sessao.setEtapa(Etapa.ENVIE_LOCALIZ);

                return r("Envie a sua localização");
            }

            if (sessao.getEtapa() == Etapa.ESCOLHA_LOCAL) {
                if (!Pattern.matches("/opcao\\d", messageText.getText())) {
                    return null;
                }

                int valor = Integer.parseInt(messageText.getText().substring(6));
                sessao.setPlaceEscolhido(valor);
                Place p = sessao.getPlace(valor);
                Response details = new Response(p.toDetail());
                Response location = new Response(p.getLatitude().doubleValue(), p.getLongitude().doubleValue());
                System.out.println("----------------------------------------" + p.getImgUrl());
                if (p.getImgUrl() != null) {
                    Response photo = Response.photo(p.getImgUrl());
                    return Arrays.asList(photo, details, location);
                }
                return Arrays.asList(details, location);
            }
        } else if (m instanceof MessageLocation) {
            UserSession sessao = dados.get(m.getUserId());
            if (sessao == null) {
                return null;
            }
            if (sessao.getEtapa() == Etapa.ENVIE_LOCALIZ) {
                sessao.setLocation((MessageLocation) m);
                sessao.setEtapa(Etapa.ESCOLHA_LOCAL);
                List<Place> places = new PlaceService().getPlaces(sessao.getLatitude(), sessao.getLongitude(), sessao.getTipoLocalEscolhido());
                sessao.setPlaces(places);
                AtomicInteger a = new AtomicInteger(0);
                return r("Esses são os locais próximos e suas distâncias:\n" + places.stream().map(l -> "/opcao" + a.incrementAndGet() + " " + l.toLine()).collect(Collectors.joining("\n")));
            }
        }

        return null;
    }
}
