import { Card, CardContent, Typography } from "@mui/material";
import { styled } from "@mui/material/styles";
import React, { useRef, useEffect } from "react";

const StyledCard = styled(Card)({
    width: 587,
    height: 470,
    overflow: "hidden",
    borderRadius: 10,
    border: "1px solid rgba(27, 44, 43, 0.5)",
    position: "relative",
});

const SlidePreview = styled("div")({
    width: "100%",
    height: 349,
    overflow: "hidden",
    position: "relative",
    transformOrigin: "top left",
    transform: "scale(0.543)",
});

interface PrezCardProps {
    title: string;
    htmlContent: string;
}

export const PrezCard: React.FC<PrezCardProps> = ({ title, htmlContent }) => {
    const previewRef = useRef<HTMLDivElement>(null);

    const getFirstSlideHtml = (html: string): string => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, "text/html");

        const firstPageDiv = doc.querySelector('div[id^="page"]');
        return firstPageDiv?.outerHTML || "";
    };

    useEffect(() => {
        if (previewRef.current) {
            const firstSlideHtml = getFirstSlideHtml(htmlContent);
            previewRef.current.innerHTML = firstSlideHtml;

            const content = previewRef.current.firstChild as HTMLElement;
            if (content) {
                content.style.transform = "scale(0.543)";
                content.style.transformOrigin = "top left";
                content.style.width = "1080px";
                content.style.height = "607px";
            }
        }
    }, [htmlContent]);

    return (
        <StyledCard>
            <SlidePreview ref={previewRef} />
            <CardContent sx={{ px: 4, pt: 1.5 }}>
                <Typography
                    variant="h2"
                    sx={{
                        fontWeight: "normal",
                        fontSize: "2.5rem",
                        color: "black",
                        fontFamily: "'Inter-Regular', Helvetica, Arial, sans-serif",
                        whiteSpace: "nowrap",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                    }}
                >
                    {title}
                </Typography>
            </CardContent>
        </StyledCard>
    );
};

export default PrezCard;