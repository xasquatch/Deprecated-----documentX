package net.xasquatch.document.service;

import org.springframework.stereotype.Service;

@Service
public class Pagination {

    private int[] calculationToPage(int pageLimit, int currentPageBlock, int totalCount) {

        int totalPageBlock = (totalCount / pageLimit);
        int startPageBlock = 1;
        int endPageBlock = 5;
        if (totalCount % pageLimit > 0)
            totalPageBlock += 1;

        if (currentPageBlock % 5 == 1) {
            startPageBlock = currentPageBlock;

        } else if (currentPageBlock % 5 == 0) {
            startPageBlock = currentPageBlock - 4;

        } else {
            startPageBlock = ((currentPageBlock / 5) * 5) + 1;

        }

        endPageBlock = startPageBlock + 4; // 4 = 5-1
        if (endPageBlock > totalPageBlock)
            endPageBlock = totalPageBlock;

        int prevPageBlock = startPageBlock - 1;

        int nextPageBlock = endPageBlock + 1;
        if (nextPageBlock > totalPageBlock)
            nextPageBlock = totalPageBlock;

        return new int[]{prevPageBlock, nextPageBlock, endPageBlock};
    }


}
