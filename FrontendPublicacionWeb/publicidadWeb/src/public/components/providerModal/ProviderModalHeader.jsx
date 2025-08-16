import Header from "./Header";
import Tabs from "./Tabs";

export default function ProviderModalHeader({ modal, onClose }) {
  const { current, tab, setTab, comments } = modal;
  return (
    <>
      <Header provider={current} onClose={onClose} />
      <Tabs
        value={tab}
        onChange={setTab}
        count={(current?.totalRatings ?? 0) + (comments?.length ?? 0)}
      />
    </>
  );
}
